package com.chinaece.gaia.util;

public interface OnFileBrowserListener
{
    public void onFileItemClick(String filename);
    public void onFlieLongItemClick(String filename);
    public void onDirItemClick(String path);
}
