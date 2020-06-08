package io.github.deposits.app.report;

import org.hibernate.Criteria;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.loader.criteria.CriteriaJoinWalker;
import org.hibernate.loader.criteria.CriteriaQueryTranslator;
import org.hibernate.persister.entity.OuterJoinLoadable;

/**
 * General utils used in tests and logs for checking queries created by JPA
 */
public class QueryTools {

    public static String toSQL(Criteria crit) {
        String sql = new BasicFormatterImpl().format(
            (new CriteriaJoinWalker(
                (OuterJoinLoadable)
                    ((CriteriaImpl)crit).getSession().getFactory().getEntityPersister(
                        ((CriteriaImpl)crit).getSession().getFactory().getImplementors(
                            ((CriteriaImpl)crit).getEntityOrClassName())[0]),
                new CriteriaQueryTranslator(
                    ((CriteriaImpl)crit).getSession().getFactory(),
                    ((CriteriaImpl)crit),
                    ((CriteriaImpl)crit).getEntityOrClassName(),
                    CriteriaQueryTranslator.ROOT_SQL_ALIAS),
                ((CriteriaImpl)crit).getSession().getFactory(),
                (CriteriaImpl)crit,
                ((CriteriaImpl)crit).getEntityOrClassName(),
                ((CriteriaImpl)crit).getSession().getLoadQueryInfluencers()
            )
            ).getSQLString()
        );

        return sql;
    }
}
