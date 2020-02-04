package org.linguafranca.pwdb.keepasshttp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jo
 */
public class LogginProxy {
    private static final AtomicInteger _COUNT = new AtomicInteger(0);

    public static Logger logger = LoggerFactory.getLogger("proxy");

    public static void main(String[] args) throws IOException {
        go();
    }

    public static void go() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(19455)) {
            do {
                logger.info("Awaiting connection");
                try (Socket clientSocket = serverSocket.accept()) {
                    Executors.newSingleThreadExecutor().submit(new Service(clientSocket));
                }
            } while (true);
        }
    }

    private static class Service implements Callable<Boolean> {
        private final Socket clientSocket;

        private Service(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public Boolean call() throws Exception {
            final String threadName = "Connection " + _COUNT.getAndIncrement();
            Thread.currentThread().setName(threadName);
            Socket forwardSocket;
            Callable<Boolean> upstream;
            Callable<Boolean> downstream;
            try (InputStream serverIS = clientSocket.getInputStream();
                    OutputStream serverOS = clientSocket.getOutputStream()) {
                forwardSocket = new Socket("192.168.1.131", 19456);
                try (InputStream clientIS = forwardSocket.getInputStream();
                        OutputStream clientOS = forwardSocket.getOutputStream()) {
                    upstream = new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            Thread.currentThread().setName(threadName + " ->");
                            logger.info("upstream starting");
                            byte[] b = new byte[1024];
                            int l;
                            while ((l = serverIS.read(b)) > -1) {
                                clientOS.write(b, 0, l);
                                clientOS.flush();
                                logger.info(new String(b, 0, l));
                            }
                            logger.info("upstream finished");
                            return true;
                        }
                    };

                    downstream = new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            Thread.currentThread().setName(threadName + " <-");
                            logger.info("downstream starting");
                            byte[] b = new byte[1024];
                            int l;
                            while ((l = clientIS.read(b)) > -1) {
                                serverOS.write(b, 0, l);
                                serverOS.flush();
                                logger.info(new String(b, 0, l));
                            }
                            logger.info("downstream finished");
                            return true;
                        }
                    };
                }
            }

            Future<Boolean> upstreamFuture = Executors.newSingleThreadExecutor().submit(upstream);
            Future<Boolean> downStreamFuture = Executors.newSingleThreadExecutor().submit(downstream);
            try {
                upstreamFuture.get();
                //forwardSocket.shutdownOutput();
                downStreamFuture.get();
                forwardSocket.shutdownOutput();
                clientSocket.shutdownOutput();
                logger.info("Connection finished");
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error processing request", e);
            }
            return true;
        }
    }
}
