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
package com.slalom.logging.compliance.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.google.common.collect.ImmutableList;
import com.slalom.logging.compliance.core.impl.LombokMaskService;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class LombokMaskServiceTest {

  @ParameterizedTest(name = "Fields: {0}, message {1} should be masked and return {2}")
  @MethodSource("source")
  public void test(List<String> fields, String message, String expected) {
    // given
    MaskService maskService = new LombokMaskService(fields);

    // when
    String actual = maskService.maskMessage(message);

    // then
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> source() {
    final User user = User.builder().login("john").password("1234").ssn("123-12-1234").build();
    return Stream.of(
        arguments(
            ImmutableList.of("password"),
            "User: " + user,
            "User: User(login=john, password=***********, ssn=123-12-1234)"),
        arguments(
            ImmutableList.of("password", "ssn"),
            "User: " + user,
            "User: User(login=john, password=***********, ssn=***********)"),
        arguments(
            ImmutableList.of("password", "ssn", "login"),
            "User: " + user,
            "User: User(login=***********, password=***********, ssn=***********)"),
        arguments(
            ImmutableList.of("password", "ssn", "login"),
            user + " log after",
            "User(login=***********, password=***********, ssn=***********) log after"));
  }
}
