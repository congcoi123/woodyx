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
package com.tenserver.woodyx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * 
 * @author kong
 *
 */

public class Asset {
	private static final String VNI_CHAR = "()á»�!?á»�áº§Ã³Ã£:1234567890$AÄ‚Ã‚BCDÄ�EÃŠGHIKLMNOÃ”Æ PQRSTUÆ¯VXYaÄƒÃ¢bcdÄ‘eÃªghiklmnoÃ´Æ¡pqrstuÆ°vxyá»™á»¥á»‘á»ƒÃ []áº­Ã¡Ãºáº»á»›á»�á»©áº¯áº¥á»Ÿá»£áº©á»‹Ã¬Ã­á»±Ã©á»“áº¡á»­áº¿áº£áº¢á»•á»”";

	public static AssetManager assetManager = new AssetManager();

	/**
	 * Loading the assets
	 */
	protected static void loading(String path, String format, String... fileNames) {
		Class<?> assetClass = null;
		if (format.equals("png") || format.equals("jpg")) {
			assetClass = Texture.class;
		}
		if (format.equals("atlas")) {
			assetClass = TextureAtlas.class;
		}
		if (format.equals("mp3")) {
			assetClass = Music.class;
		}
		if (format.equals("ogg")) {
			assetClass = Sound.class;
		}

		for (int i = 0; i < fileNames.length; i++) {
			assetManager.load(path + fileNames[i] + "." + format, assetClass);
		}
	}

	/**
	 * Unload the assets
	 */
	protected static void unload(String path, String format, String... fileNames) {
		for (int i = 0; i < fileNames.length; i++) {
			assetManager.unload(path + fileNames[i] + "." + format);
		}
	}

	/**
	 * Get the key frames
	 */
	public static TextureRegion[] getKeyFrames(TextureAtlas textureAtlas, String keyFrame, int startFrame,
			int endFrame) {
		int size = endFrame - startFrame + 1;

		TextureRegion[] trRegions = new TextureRegion[size];

		for (int i = 0; i < size; i++) {
			trRegions[i] = textureAtlas.findRegion(keyFrame + (i + startFrame));
		}

		return trRegions;
	}

	/**
	 * Get the animation
	 */
	public static Animation loadAnimation(float duration, TextureRegion[]... keyFrames) {
		int size = 0;
		for (int i = 0; i < keyFrames.length; i++) {
			size += keyFrames[i].length;
		}

		int k = 0;
		TextureRegion[] trRegions = new TextureRegion[size];

		for (int i = 0; i < keyFrames.length; i++) {
			for (int j = 0; j < keyFrames[i].length; j++) {
				trRegions[k] = keyFrames[i][j];
				k++;
			}
		}

		// create the animation
		Animation animation = new Animation(duration, trRegions);

		return animation;
	}

	/**
	 * Make the bitmap font from true type font
	 */
	protected static BitmapFont getFontGenerate(String file, int size, boolean isVNIFont) {
		BitmapFont font;
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(file));
		if (isVNIFont)
			font = generator.generateFont(size, VNI_CHAR, false);
		else
			font = generator.generateFont(size);
		generator.dispose();
		return font;
	}

	/**
	 * Load the particle effect
	 */
	public static ParticleEffect loadParticleEffect(String folder, String fileName) {
		ParticleEffect effect;

		effect = new ParticleEffect();
		effect.load(Gdx.files.internal(folder + "/" + fileName), Gdx.files.internal(folder));

		return effect;
	}

	/**
	 * Load the particle effect
	 */
	public static ParticleEffect loadParticleEffect(String folder, TextureAtlas atlas) {
		ParticleEffect effect;

		effect = new ParticleEffect();
		effect.load(Gdx.files.internal(folder), atlas);

		return effect;
	}

	/**
	 * Split the @see {@link TextureRegion} for Animation
	 */
	public static TextureRegion[] frameSplit(TextureRegion textureRegion, int rows, int cols) {

		TextureRegion[] result;
		int tileWidth = textureRegion.getRegionWidth() / cols;
		int tileHeight = textureRegion.getRegionHeight() / rows;

		TextureRegion[][] temp = textureRegion.split(tileWidth, tileHeight);
		result = new TextureRegion[cols * rows];

		int index = 0;

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				result[index++] = temp[i][j];
			}
		}

		return result;
	}

	/**
	 * Dispose the AssetsManager
	 */
	public static void dispose() {
		assetManager.dispose();
	}
}
