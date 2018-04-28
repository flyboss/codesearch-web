package com.dao;

import com.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Created by flyboss on 2018/4/26.
 */
public class DaoUtil {
    public static void truncateTable(String table){
        Session session = null;
        Transaction transaction = null;
        String hql = String.format("truncate table %s", table);
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.createNativeQuery(hql).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }

    }
}
