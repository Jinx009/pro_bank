package com.rongdu.common.util.jcaptcha;

import com.octo.captcha.engine.image.ListImageCaptchaEngine;

public class SimpleImageEngine extends ListImageCaptchaEngine {
	@Override
	protected void buildInitialFactories() {
		/*
		 * public static final int minAcceptedWordLength=4; public static final int maxAcceptedWordLength=5; public
		 * static final int imgWidth=100; public static final int imgHeight=27; public static final int minFontSize=20;
		 * public static final int maxFontSize=25; public static final Font font=new
		 * Font("@YaHei Consolas Hybrid",Font.PLAIN ,(minFontSize+maxFontSize)/2); public static final String
		 * captchaContent= "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"; private static
		 * NonLinearTextPaster testPaster=new NonLinearTextPaster(minAcceptedWordLength, maxAcceptedWordLength, new
		 * Color(60,60,60)); private static GradientBackgroundGenerator backgroundGen=new
		 * GradientBackgroundGenerator(imgWidth, imgHeight, new Color(239,246,236), new Color(239,246,236)); private
		 * static RandomFontGenerator fontGen=new RandomFontGenerator(minFontSize, maxFontSize,new Font[]{font});
		 * private static RandomWordGenerator wordGen=new RandomWordGenerator(captureContent); private static
		 * ComposedWordToImage wordToImage=new ComposedWordToImage(fontGen,backgroundGen,testPaster); private static
		 * GimpyFactory captchaFacotry=new GimpyFactory(wordGen, wordToImage); private static CaptchaFactory[]
		 * factories=new GimpyFactory[]{captchaFacotry};
		 */
	}

}
