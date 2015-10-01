package pt.uc.dei.aor.proj.tools;

/* 
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author 
 */
@Named
@SessionScoped

public class ViewImgs implements Serializable{

    private List<String> images;

    /**
     * Initialize ImagesView for entering main page
     */
    @PostConstruct
    public void init() {
        //Introduce images name into a String array
        images = new ArrayList<>();
        String name="";
        for (int i = 1; i <= 7; i++) {
        	name="projcrit" + i + ".jpg";
            images.add(name);
            Logger.getLogger(ViewImgs.class.getName()).log(Level.INFO, "Inside init() where was added img name="+name);
        }
    }

    public List<String> getImages() {
        return images;
    }

}

