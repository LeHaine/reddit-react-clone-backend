package clone.reddit.util;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

/**
 * Created by colt on 7/11/18.
 */
public class Base36IdGenerator implements IdentifierGenerator, Configurable {

    private String prefix;
    private int initialValue;

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        prefix = params.getProperty("prefix");
        initialValue = Integer.parseInt(params.getProperty("initialValue"));
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {
        String query = String.format("select count(%s) from %s",
                session.getEntityPersister(obj.getClass().getName(), obj)
                        .getIdentifierPropertyName(),
                obj.getClass().getSimpleName());

        Long max = Long.parseLong(session.createQuery(query).getSingleResult().toString());
        max += initialValue;

        String base36 = Long.toString(max + 1, 36);

        return prefix + "_" + base36;
    }

}
