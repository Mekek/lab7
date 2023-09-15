package responseLogic;

import exceptions.NotAvailableServerException;
import responses.BaseResponse;
import responses.ErrorResponse;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class ResponseReader {
    final InputStream in;

    public ResponseReader(InputStream in) {
        this.in = in;
    }

    public BaseResponse readObject() throws IOException, ClassNotFoundException, NotAvailableServerException {
        try {
            ObjectInputStream ois = new ObjectInputStream(in);
            BaseResponse result = (BaseResponse) ois.readObject();
            if (result instanceof ErrorResponse)
                throw new NotAvailableServerException(((ErrorResponse) result).getMsg());
            return result;
        } catch (EOFException e) {
            return new ErrorResponse("Ответ от сервера не уместился в буфер. Возможно, коллекция получилась слишком большая =(");
        }
    }
}