/*
 * SUBFRAME - Simple Java Benchmarking Framework
 * Copyright (C) 2012 - 2013 Fabian Prasser
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.File;

import junit.framework.TestCase;

/**
 * Base class for JUnit tests
 * @author Fabian Prasser
 */
public abstract class TestBase extends TestCase {

    protected void checkAndDelete(String file){
        File ffile = new File(file);
        if (!ffile.exists()){
            fail("File: " + file + " does not exist");
        } else {
            ffile.delete();
        }
    }
}
