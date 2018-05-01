package com.dao;

import com.entity.FuncIndex;
import com.entity.Project;
import com.main.Main;
import com.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

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

    public Project find(String name) {
        Session session = null;
        try {
            session = Main.sessionFactory.openSession();
            Query query = session.createQuery("from Project where name = :name");
            query.setParameter("name", name);
            List<Project> projects = query.getResultList();
            if (projects.size() == 0) {
                return null;
            } else {
                return projects.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Project> getAll(){
        Session session = null;
        try {
            session = Main.sessionFactory.openSession();
            Query query = session.createQuery("from Project ");
            List<Project> projects = query.getResultList();
            return projects;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void update(Project project){
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(project);
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
