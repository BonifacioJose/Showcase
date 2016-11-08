package br.com.twsistemas.showcasepf6.view;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.extensions.event.ImageAreaSelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author José
 */
@Named
@SessionScoped
public class AvatarUploadController implements Serializable {

    private UploadedFile file;
    private BufferedImage croppedImage;
    private int height;
    private int width;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public StreamedContent getImagestream() {
        if (file != null) {
            ByteArrayInputStream bytesIs = new ByteArrayInputStream(file.getContents());
            try {
                BufferedImage buf = ImageIO.read(bytesIs);
                int heightAux = buf.getHeight();
                int widthAux = buf.getWidth();
                int percent;
                if (heightAux >= widthAux) {
                    percent = widthAux / (heightAux / 100);
                    if (heightAux > 400) {
                        int diferenca = heightAux - 400;
                        heightAux = heightAux - diferenca;
                    }
                    widthAux = percent * (heightAux / 100);
                } else {
                    percent = heightAux / (widthAux / 100);
                    if (widthAux > 400) {
                        int diferenca = widthAux - 400;
                        widthAux = widthAux - diferenca;
                    }
                    heightAux = percent * (widthAux / 100);
                }
                width = widthAux;
                height = heightAux;
            } catch (IOException ex) {
                Logger.getLogger(AvatarUploadController.class.getName()).log(Level.SEVERE, null, ex);
            }
            DefaultStreamedContent retorno = new DefaultStreamedContent(new ByteArrayInputStream(file.getContents()), file.getContentType());
            return retorno;
        }
        return null;
    }

    public StreamedContent getImagestream2() {
        if (croppedImage != null) {
            System.out.println("teste666");
            byte[] byteArray = ((DataBufferByte) croppedImage.getData().getDataBuffer()).getData();
            DefaultStreamedContent retorno = new DefaultStreamedContent(new ByteArrayInputStream(byteArray));
            return retorno;
        }
        return null;
    }

    public void handleFileUpload(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        file = event.getFile();
    }

    public void selectEndListener(final ImageAreaSelectEvent e) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Area selected",
                "X1: " + e.getX1()
                + ", X2: " + e.getX2()
                + ", Y1: " + e.getY1()
                + ", Y2: " + e.getY2()
                + ", Image width: " + e.getImgWidth()
                + ", Image height: " + e.getImgHeight());

        FacesContext.getCurrentInstance().addMessage(null, msg);
        try {
            croppedImage = cropImage(ImageIO.read(new ByteArrayInputStream(file.getContents())), e.getX1(), e.getY1(), e.getX2(), e.getY2());
        } catch (IOException ex) {
            Logger.getLogger(AvatarUploadController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private BufferedImage cropImage(BufferedImage src, int sourceX, int sourceY, int width, int height) {        
        BufferedImage dest = src.getSubimage(sourceX, sourceY, width, height);
        return dest;
    }
}
