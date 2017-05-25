package com.rongdu.p2psys.account.model.payment.xspay;

import java.io.ObjectInputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAAlgorithms {
	static final String algname = "SHA1withRSA";
	static final String keyalgname = "RSA";

	public String getAlgname() {
		return "SHA1withRSA";
	}

	public String getKeyAlgname() {
		return "RSA";
	}

	public String genSignature(byte[] src, byte[] usekey) throws Exception {
		return genSignature(src, getPrivateKey(usekey));
	}

	public String genSignature(byte[] src, PrivateKey usekey, String provider) throws Exception {
		Signature sig = Signature.getInstance(getAlgname(), provider);
		sig.initSign(usekey);
		sig.update(src);
		return ByteArrayUtil.toHexString(sig.sign());
	}

	public boolean verifySignature(byte[] src, String dest, PublicKey usekey, String provider) throws Exception {
		Signature sigcheck = Signature.getInstance(getAlgname(), provider);
		sigcheck.initVerify(usekey);
		sigcheck.update(src);
		return sigcheck.verify(ByteArrayUtil.toByteArray(dest));
	}

	public String genSignature(byte[] src, Key usekey) throws Exception {
		return genSignature(src, (PrivateKey) usekey);
	}

	public boolean verifySignature(byte[] src, String dest, byte[] usekey) throws Exception {
		return verifySignature(src, dest, getPublicKey(usekey));
	}

	public String genSignature(byte[] src, ObjectInputStream usekey) throws Exception {
		PrivateKey mykey = (PrivateKey) usekey.readObject();
		usekey.close();
		Signature sig = Signature.getInstance(getAlgname());
		sig.initSign(mykey);
		sig.update(src);
		return ByteArrayUtil.toHexString(sig.sign());
	}

	public String genSignature(byte[] src, PrivateKey usekey) throws Exception {
		Signature sig = Signature.getInstance(getAlgname());
		sig.initSign(usekey);
		sig.update(src);
		return ByteArrayUtil.toHexString(sig.sign());
	}

	public boolean verifySignature(byte[] src, String dest, ObjectInputStream usekey) throws Exception {
		PublicKey mypublickey = (PublicKey) usekey.readObject();
		usekey.close();
		Signature sigcheck = Signature.getInstance(getAlgname());
		sigcheck.initVerify(mypublickey);
		sigcheck.update(src);
		return sigcheck.verify(ByteArrayUtil.toByteArray(dest));
	}

	public boolean verifySignature(byte[] src, String dest, PublicKey usekey) throws Exception {
		Signature sigcheck = Signature.getInstance(getAlgname());
		sigcheck.initVerify(usekey);
		sigcheck.update(src);
		return sigcheck.verify(ByteArrayUtil.toByteArray(dest));
	}

	PublicKey getPublicKey(byte[] key) throws Exception {
		KeyFactory kFactory = KeyFactory.getInstance(getKeyAlgname());
		X509EncodedKeySpec myPubKeySpec = new X509EncodedKeySpec(key);

		PublicKey mPubKey = kFactory.generatePublic(myPubKeySpec);
		return mPubKey;
	}

	PrivateKey getPrivateKey(byte[] key) throws Exception {
		KeyFactory kFactory = KeyFactory.getInstance(getKeyAlgname());
		PKCS8EncodedKeySpec myPubKeySpec = new PKCS8EncodedKeySpec(key);
		PrivateKey mPrivateKey = kFactory.generatePrivate(myPubKeySpec);
		return mPrivateKey;
	}
}
