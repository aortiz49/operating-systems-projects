package communication_protocol.gloadTests;

import communication_protocol.client.Client;
import communication_protocol.client.Client_NoSecurity;
import org.bouncycastle.operator.OperatorCreationException;
import uniandes.gload.core.Task;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class ClientServerTask extends Task {
    @Override
    public void execute() {
        try {
            Client client = new Client();
            //Client_NoSecurity client = new Client_NoSecurity();
            client.process();
        } catch (IOException | NoSuchAlgorithmException | OperatorCreationException | CertificateException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void fail() {
        System.out.println(Task.MENSAJE_FAIL);

    }

    @Override
    public void success() {
        System.out.println(Task.OK_MESSAGE);

    }
}