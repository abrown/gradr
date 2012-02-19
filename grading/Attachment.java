package grading;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author andrew
 */
public class Attachment {

    private String name;
    private String content;
    private File local_file;

    public Attachment(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getLocalFile() {
        return local_file;
    }

    public void setLocalFile(File local) {
        this.local_file = local;
    }

    public void save(Path directory) {
        String content = null;
        // change package name
//        if (this.getName().endsWith(".java")) {
//            String packageName = directory.toString().replace(File.separator, ".");
//            content = this.changePackageName(packageName);
//        } else {
//            content = this.getContent();
//        }
        content = this.getContent();
        File f = directory.resolve(this.getName()).toFile();
        try{
            // create file
            if( f.exists() ) f.delete();
            f.createNewFile();
            // save contents
            BufferedWriter out = new BufferedWriter(new FileWriter(f));
            out.write(content);
            out.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        // save to this
        this.setLocalFile(f);
    }

    public String changePackageName(String packageName) {
        if (this.getContent().contains("package ")) {
            content = this.getContent().replaceFirst("package .+;", "package " + packageName + ";");
        } else {
            content = "package " + packageName + ";\n\n" + this.getContent();
        }
        return content;
    }

    public String toString() {
        return this.name + " = '" + this.content.substring(0, 20).replace("\n", " ") + "...'";
    }
}
