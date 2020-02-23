/*
The MIT License

Copyright (c) 2014 kong <congcoi123@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.tenserver.woodyx.input;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

/**
 * 
 * @author kong
 *
 */
public class XInputProcessor implements InputProcessor {

	private ArrayList<InputProcessor> list = new ArrayList<InputProcessor>();
	private static byte index;

	public XInputProcessor(InputProcessor... inputProcessors) {
		addInputProcessor(inputProcessors);
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public boolean keyDown(int keycode) {
		for (index = 0; index < list.size(); index++) {
			list.get(index).keyDown(keycode);
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		for (index = 0; index < list.size(); index++) {
			list.get(index).keyDown(keycode);
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		for (index = 0; index < list.size(); index++) {
			list.get(index).keyTyped(character);
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		for (index = 0; index < list.size(); index++) {
			list.get(index).touchDown(screenX, screenY, pointer, button);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		for (index = 0; index < list.size(); index++) {
			list.get(index).touchUp(screenX, screenY, pointer, button);
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		for (index = 0; index < list.size(); index++) {
			list.get(index).touchDragged(screenX, screenY, pointer);
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		for (index = 0; index < list.size(); index++) {
			list.get(index).mouseMoved(screenX, screenY);
		}
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		for (index = 0; index < list.size(); index++) {
			list.get(index).scrolled(amount);
		}
		return false;
	}

	public void addInputProcessor(InputProcessor... inputProcessors) {
		for (InputProcessor input : inputProcessors) {
			list.add(input);
		}
	}

	public void removeInputProcessor(InputProcessor input) {
		if (list.contains(input))
			list.remove(input);
	}

	public void removeAllInputProcessor() {
		list.clear();
	}

}
