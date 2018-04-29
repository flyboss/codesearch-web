package com.dao;

import com.entity.FuncIndex;
import com.entity.Project;
import com.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Created by flyboss on 2018/4/29.
 */
public class ProjectDao {
    public Project add(Project project) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(project);
            transaction.commit();
            return project;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
