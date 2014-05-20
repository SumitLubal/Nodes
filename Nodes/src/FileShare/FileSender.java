package FileShare;
import java.io.File;
import java.util.List;


public class FileSender {
	Client client;
	String baseDirectory ;
	CopyDialog copy;
	public void sendDroppedFiles(List<File> droppedFiles, Client client) {
		this.client = client;
		copy.setVisible(true);
		for(int i=0;i<droppedFiles.size();i++){
			if(droppedFiles.get(i).isDirectory()){
				baseDirectory = droppedFiles.get(i).getParentFile().getAbsolutePath();
				sendFolder(droppedFiles.get(i));
			}else if(droppedFiles.get(i).isFile()){
				sendFile(droppedFiles.get(i),"/"+droppedFiles.get(i).getName());
			}
		}
		copy.setVisible(false);
	}

	private void sendFile(File file,String path) {
		try {
			client.send(file,path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendFolder(File dir) {
		String directory = dir.getAbsolutePath(); //
		File files[] = new File(directory).listFiles();
		String path;
		for(File file:files){
			if(file.isDirectory()){
				sendFolder(file);
			}else if(file.isFile()){
				path = getDirectoryAndFile(file.getAbsolutePath());
				sendFile(file,path);
			}
		}
	}

	private String getDirectoryAndFile(String path) {
		String statement = null;
		statement = path.substring(baseDirectory.length());
		System.out.println(statement + " For directory");
		return statement;
		
	}

}
