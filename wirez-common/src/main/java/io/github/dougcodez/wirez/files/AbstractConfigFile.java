/**
 * MIT License
 *
 * Copyright (c) 2022-2023 Douglas (dougcodez)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.dougcodez.wirez.files;

import dev.dejvokep.boostedyaml.YamlDocument;

import java.io.File;
import java.io.IOException;

public abstract class AbstractConfigFile implements IConfigFile{

        private YamlDocument customConfig;

        public void initFile(File fileDestination, String fileName) {
            createFile(fileDestination, fileName);
            initData();
            save();
        }

        @Override
        public void createFile(File fileDestination, String fileName) {
            File file = new File(fileDestination, fileName);
            try {
                customConfig = YamlDocument.create(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        @Override
        public void save() {
            try {
                customConfig.save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void reload() {
            try {
                customConfig.reload();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public YamlDocument getConfigFile() {
            return customConfig;
        }


        public abstract void initData();
}
