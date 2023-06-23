package responseLogic;

import exceptions.NotAvailableServerException;
import responses.BaseResponse_;
import responses.ErrorResponse_;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class ResponseReader {
    final InputStream in;

    public ResponseReader(InputStream in) {
        this.in = in;
    }

    public BaseResponse_ readObject() throws IOException, ClassNotFoundException, NotAvailableServerException {
        try {
            ObjectInputStream ois = new ObjectInputStream(in);
            BaseResponse_ result = (BaseResponse_) ois.readObject();
            if (result instanceof ErrorResponse_)
                throw new NotAvailableServerException(((ErrorResponse_) result).getMsg());
            return result;
        } catch (EOFException e) {
            return new ErrorResponse_("Ответ от сервера не уместился в буфер. Возможно, коллекция получилась слишком большая =(");
        }
    }
}