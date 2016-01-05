/**
 * 
 */


import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.domain.mm.rd.massnewsku.ms.NotFoundException;

/**
 * @author shadowwa
 *
 */
@Provider
public final class NotFoundExceptionHandler implements ExceptionMapper< NotFoundException> {
	@Override
	public Response toResponse(final NotFoundException exception) {
		System.out.println("Oh. My. Hawd");
		return Response.status(Status.BAD_REQUEST)
				.entity(new ErrorMessage(exception.getCode(), exception.getMessage()))
				.type(MediaType.APPLICATION_JSON).build();
	}

}
