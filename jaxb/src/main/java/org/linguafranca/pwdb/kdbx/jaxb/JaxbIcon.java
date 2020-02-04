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

package org.linguafranca.pwdb.kdbx.jaxb;

import org.linguafranca.pwdb.Icon;

/**
 * @author jo
 */
@SuppressWarnings("WeakerAccess")
public class JaxbIcon implements Icon{
    private int index;

    public JaxbIcon(int iconID) {
       index = iconID;
    }

    public JaxbIcon() {
        index = 0;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }


    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + this.index;
        return hash;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JaxbIcon other = (JaxbIcon) obj;
        if (this.index != other.index) {
            return false;
        }
        return true;
    }
}
