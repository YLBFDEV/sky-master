package com.skytech.android.util.property;

public class ExtStoragePropertyManagement extends AbstractPropertyManagement
		implements PropertyManagement {
	
	private String EXT_STORAGE_ROOT = android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath();

	public ExtStoragePropertyManagement(String configPath) {
		/**
		 * @todo 一次性装载所有应用property至 properties
		 */
	}
	
	@Override
	public boolean setString(String name, String val) {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public boolean remove(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public boolean contains(String name) {
		// TODO Auto-generated method stub
		return false;
	}	
}
