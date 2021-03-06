/**
 * MIT License
 *
 * <p>Copyright (c) 2020 Slalom LLC
 *
 * <p>Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * <p>The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.slalom.logging.compliance.core.impl;

import java.util.List;
import java.util.regex.Pattern;

/** This class setup the JSON logic that needs to be apply to mask JSON objects. */
public class JsonMaskService extends BaseMaskService {

  private static final String JSON_REPLACEMENT_REGEX = "\"$1\"$2:$3\"" + DEFAULT_MASK + "\"$6";
  private static final String JSON_PATTERN =
      "\"(%s)\"([\\s]*):([\\s]*)(\"([^\"]+)\"|[\\d]+|true|false|null)([\\s]*)";

  private final Pattern pattern;

  public JsonMaskService(final List<String> fields) {
    super(fields);
    this.pattern = Pattern.compile(String.format(JSON_PATTERN, fieldRegex));
  }

  @Override
  public String maskMessage(final String message) {
    return maskMessage(message, pattern, JSON_REPLACEMENT_REGEX);
  }
}
