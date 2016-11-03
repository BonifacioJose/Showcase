package br.com.twsistemas.showcasepf6.view;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Bonifácio
 */
@Named
@ViewScoped
public class IndexController implements Serializable {
    
    private String console;

    public String getConsole() {
        return console;
    }

    public void setConsole(String console) {
        this.console = console;
    }
    
}
