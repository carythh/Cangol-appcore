package mobi.cangol.mobile.service.download;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mobi.cangol.mobile.utils.FileUtils;
import android.content.Context;

public class ApkDownloadExecutor extends DownloadExecutor {
	private String apkDir;
	public ApkDownloadExecutor(String name) {
		super(name);
		initData();
		initEvent();
	}
	@Override
	public DownloadResource createResource(String url, String name) {
		DownloadResource resource=new DownloadResource(url,name);
		resource.setKey(""+(url+name).hashCode());
		resource.setLocalPath(apkDir);
		String confFile=apkDir+File.separator+name+Download.SUFFIX_CONFIG;
		String sourceFile=apkDir+File.separator+name+Download.SUFFIX_SOURCE;
		//执行创建文件
		
		resource.setConfFile(confFile);
		resource.setSourceFile(sourceFile);
		return resource;
	}
	@Override
	public ArrayList<DownloadResource> scanResource() {
		List<File> fileList=FileUtils.searchBySuffix(new File(apkDir), null, Download.SUFFIX_SOURCE);
		DownloadResource resource=null;
		ArrayList<DownloadResource> resList=new ArrayList<DownloadResource>();
		for (int i = 0; i < fileList.size(); i++) {   
			resource =DownloadResource.initFromFile(fileList.get(i).getAbsolutePath());
			if(null!=resource){
				if(resource.getFileLength()!=resource.getCompleteSize()||resource.getFileLength()==0){
					resource.setStatus(Download.STATUS_STOP);
					resource.save();
					resList.add(resource);
				}
			}
		}
		return resList;
	}
	
	@Override
	public DownloadNotification notification(Context context,DownloadResource resource) {
		
		return null;
	}
	private void initData() {
		
	}
	private void initEvent() {
		this.setDownloadEvent(new DownloadEvent(){

			@Override
			public void onStart(DownloadResource resource) {
				
			}

			@Override
			public void onFinish(DownloadResource resource) {
				
			}

			@Override
			public void onFailure(DownloadResource resource) {
				
			}
			
		});
		
	}

}
