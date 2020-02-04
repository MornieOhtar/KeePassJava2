/*
 * Copyright 2015 Jo Rabin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.linguafranca.pwdb.kdbx;

import org.junit.Test;
import org.linguafranca.pwdb.kdbx.stream_3_1.KdbxHeader;
import org.linguafranca.pwdb.kdbx.stream_3_1.KdbxSerializer;
import org.linguafranca.pwdb.Credentials;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author jo
 */
public class KdbxKeyFileTest {

    @Test
    public void testLoad() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("KeyFileDatabase.key")) {
            byte[] key = KdbxKeyFile.load(is);
            assertNotNull(key);
            assertEquals(32, key.length);
        }
    }

    /*
    Test for empty password
     */
    @Test
    public void testEmptyPasswordCreds() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("EmptyPassword.kdbx")) {
            Credentials credentials = new KdbxCreds(new byte[0]);
            try (InputStream dis = KdbxSerializer.createUnencryptedInputStream(credentials, new KdbxHeader(), is)) {
                byte[] buffer = new byte[1024];
                while (dis.available() > 0) {
                    int read = dis.read(buffer);
                    if (read == -1) {
                        break;
                    }
                    System.out.write(buffer, 0, read);
                }
            }
        }
    }


    /**
     Test for empty password with key
     */
    @Test
    public void testEmptyPasswordKeyCreds() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("EmptyPasswordWithKey.kdbx");
                InputStream keyFileIS = getClass().getClassLoader().getResourceAsStream("EmptyPasswordWithKey.key")) {
            Credentials credentials = new KdbxCreds(new byte[0], keyFileIS);
            try (InputStream dis = KdbxSerializer.createUnencryptedInputStream(credentials, new KdbxHeader(), is)) {
                byte[] buffer = new byte[1024];
                while (dis.available() > 0) {
                    int read = dis.read(buffer);
                    if (read == -1) {
                        break;
                    }
                    System.out.write(buffer, 0, read);
                }
            }
        }
    }

    /**
     Test for no master password
     */
    @Test
    public void testNoPasswordKeyCreds() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("NoPasswordWithKey.kdbx");
                InputStream keyFileIS = getClass().getClassLoader().getResourceAsStream("NoPasswordWithKey.key")) {
            Credentials credentials = new KdbxCreds(keyFileIS);
            try (InputStream dis = KdbxSerializer.createUnencryptedInputStream(credentials, new KdbxHeader(), is)) {
                byte[] buffer = new byte[1024];
                while (dis.available() > 0) {
                    int read = dis.read(buffer);
                    if (read == -1) {
                        break;
                    }
                    System.out.write(buffer, 0, read);
                }
            }
        }
    }

    /*
    Test for empty password
     */
    @Test
    public void testEmptyPassword() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("EmptyPassword.kdbx")) {
            Credentials credentials = new KdbxCreds(new byte[0]);
            try (InputStream dis = KdbxSerializer.createUnencryptedInputStream(credentials, new KdbxHeader(), is)) {
                byte[] buffer = new byte[1024];
                while (dis.available() > 0) {
                    int read = dis.read(buffer);
                    if (read == -1) {
                        break;
                    }
                    System.out.write(buffer, 0, read);
                }
            }
        }
    }
}