package severeLobster.frontend.dialogs;

import infrastructure.ResourceManager;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import java.awt.Frame;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Dialog zur Aenderung des Avatars
 *
 * @author Lars Schlegelmilch
 */
public class AvatarAendernDialog extends JDialog {

    private final ResourceManager resourceManager = ResourceManager.get();

    private Frame owner;
    private Map<ImageIcon, URL> avatarMap = new HashMap<ImageIcon, URL>();

    private final ImageIcon[] avatare = {
            resourceManager.getAvatarPreviewImageIcon("spielinfo_m1.jpg"),
            resourceManager.getAvatarPreviewImageIcon("spielinfo_m2.jpg"),
            resourceManager.getAvatarPreviewImageIcon("spielinfo_m3.jpg"),
            resourceManager.getAvatarPreviewImageIcon("spielinfo_w1.jpg"),
            resourceManager.getAvatarPreviewImageIcon("spielinfo_w2.jpg"),
            resourceManager.getAvatarPreviewImageIcon("spielinfo_w3.jpg")
    };

    private final URL[] avatarURLs = {
            resourceManager.getAvatarURL("spielinfo_m1.jpg"),
            resourceManager.getAvatarURL("spielinfo_m2.jpg"),
            resourceManager.getAvatarURL("spielinfo_m3.jpg"),
            resourceManager.getAvatarURL("spielinfo_w1.jpg"),
            resourceManager.getAvatarURL("spielinfo_w2.jpg"),
            resourceManager.getAvatarURL("spielinfo_w3.jpg")
    };

    public AvatarAendernDialog(Frame owner) {
        this.owner = owner;
        if (avatare.length != avatarURLs.length) {
            throw new IllegalStateException();
        }
        for (int i = 0; i < avatarURLs.length; i++) {
            avatarMap.put(avatare[i], avatarURLs[i]);
        }
    }

    public URL showDialog() {
        ImageIcon result = (ImageIcon) JOptionPane.showInputDialog(owner,
                resourceManager.getText("change.avatar.text"),
                resourceManager.getText("change.avatar.title"),
                JOptionPane.INFORMATION_MESSAGE,
                null, avatare, avatare[0]);
        return avatarMap.get(result);
    }
}
