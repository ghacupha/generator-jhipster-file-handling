package <%= packageName %>.internal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * This is a message transfer object for delete requests where an Id of an entity to be deleted is given.
 * The id to be deleted is here represented by the field intuitively named as id
 */
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class DeleteMessageDTO implements TokenizableMessage<String>, Serializable {
    private static final long serialVersionUID = 7581295114110315260L;
    private long id;
    private String description;
    private String messageToken;
    private long timestamp;
}
