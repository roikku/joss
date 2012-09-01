package nl.t42.openstack.command.objectstorage;

import nl.t42.openstack.command.core.AbstractSecureCommand;
import nl.t42.openstack.command.core.CommandException;
import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.command.identity.access.Access;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;

public class DeleteContainerCommand extends AbstractSecureCommand<HttpDelete, String[]> {

    public DeleteContainerCommand(HttpClient httpClient, Access access, String containerName) {
        super(httpClient, access.getInternalURL() + "/" + containerName, access.getToken());
    }

    @Override
    protected HttpDelete createRequest(String url) {
        return new HttpDelete(url);
    }

    @Override
    protected void checkHttStatusCode(int httpStatusCode) {
        if (httpStatusCode == HttpStatus.SC_NO_CONTENT) {
            return;
        } else if (httpStatusCode == HttpStatus.SC_NOT_FOUND) {
            throw new CommandException(httpStatusCode, CommandExceptionError.CONTAINER_DOES_NOT_EXIST);
        } else if (httpStatusCode == HttpStatus.SC_CONFLICT) {
            throw new CommandException(httpStatusCode, CommandExceptionError.CONTAINER_NOT_EMPTY);
        }
        throw new CommandException(httpStatusCode, CommandExceptionError.UNKNOWN);
    }

}
