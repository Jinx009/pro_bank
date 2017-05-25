package com.rongdu.p2psys.account.model.payment.xspay;

public class HashAlgorithms {
	public static int PJWHash(String str) {
		int bitsInUnsignedInt = 32;
		int threeQuarters = bitsInUnsignedInt * 3 / 4;
		int oneEighth = bitsInUnsignedInt / 8;
		int highBits = -1 << bitsInUnsignedInt - oneEighth;
		int hash = 0;
		int test = 0;

		for (int i = 0; i < str.length(); i++) {
			hash = (hash << oneEighth) + str.charAt(i);

			if ((test = hash & highBits) == 0)
				continue;
			hash = (hash ^ test >> threeQuarters) & (highBits ^ 0xFFFFFFFF);
		}

		return hash & 0x7FFFFFFF;
	}
}
