package com.tcs.ehs.utils;



public class Constants {
	
;
	

	public static enum CIMPOS {
		assetname {
			@Override
			public String toString() {
				return "assetname";
			}
		},
		floorNo {
			@Override
			public String toString() {
				return "floorNo";
			}
		},
		name {
			@Override
			public String toString() {
				return "name";
			}
		};	
		
		public static String[] list() {
			CIMPOS[] cimpos = values();
			String[] names = new String[cimpos.length];

			for (int i = 0; i < cimpos.length; i++) {
				names[i] = cimpos[i].toString();
			}

			return names;
		}
		
	};	
		
	
	
	

	
	//public static String hygieneAreas[] = new String[] { "SMT Line 1", "SMT Line 2", "Hygiene Production Ground Floor" };
	//public static String aqiAreas[] = new String[] { "SMT Area", "Production Ground Floor", "Near Soldering Machine", "Heller-Machine", "Soltech-Machine", "Reflow-Ovan", "Wave-Soldering-Machine" };
}