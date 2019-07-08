/**
 * MIT License
 *
 * Copyright (c) 2017 TRIOLOGY GmbH
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
package de.triology.cb.decorator;

import de.triology.cb.Command;
import de.triology.cb.CommandBus;
import de.triology.cb.EchoCommand;
import io.micrometer.core.instrument.Counter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MicrometerCountingCommandBusTest {

  @Mock
  private CommandBus decorated;

  @Mock
  private Counter counterOne;

  @Mock
  private Counter counterTwo;

  private MicrometerCountingCommandBus commandBus;

  @Before
  public void setUp() {
    commandBus = new MicrometerCountingCommandBus(decorated, command -> {
      if (EchoCommand.class.isAssignableFrom(command)) {
        return counterOne;
      }
      return counterTwo;
    });
  }

  @Test
  public void execute() {
    commandBus.execute(new EchoCommand("hello"));
    commandBus.execute(new EchoCommand("hello 2"));
    commandBus.execute(new OtherCommand());
    commandBus.execute(new OtherCommand());

    verify(counterOne, times(2)).increment();
    verify(counterTwo, times(2)).increment();
  }

  public static class OtherCommand implements Command<Void> {

  }

}