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
 * Created by flyboss on 2018/4/25.
 */
public class FuncIndexDao {
    public FuncIndex add(FuncIndex funcIndex) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(funcIndex);
            transaction.commit();
            return funcIndex;
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

    public FuncIndex find(String word) {
        Session session = null;
        try {
            session = Main.sessionFactory.openSession();
            Query query = session.createQuery("from FuncIndex where word = :word");
            query.setParameter("word", word);
            List<FuncIndex> funcIndices = query.getResultList();
            if (funcIndices.size() == 0) {
                return null;
            } else {
                return funcIndices.get(0);
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

    public FuncIndex find(int id) {
        Session session = null;
        try {
            session = Main.sessionFactory.openSession();
            Query query = session.createQuery("from FuncIndex where id = :id");
            query.setParameter("id", id);
            List<FuncIndex> funcIndices = query.getResultList();
            if (funcIndices.size() == 0) {
                return null;
            } else {
                return funcIndices.get(0);
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

    public void update(FuncIndex funcIndex){
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(funcIndex);
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

    public double findMaxIdf(boolean isName){
        Session session = null;
        String hql=null;
        if (isName){
            hql="select max(nameIdf) FROM FuncIndex ";
        }else{
            hql="select max(bodyIdf) FROM FuncIndex ";
        }
        try {
            session = Main.sessionFactory.openSession();
            Query query = session.createQuery(hql);
            double maxIdf = (double)query.getResultList().get(0);
            return maxIdf;
        } catch (Exception e) {
            return 10;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<FuncIndex> getAll(){
        Session session = null;
        try {
            session = Main.sessionFactory.openSession();
            Query query = session.createQuery("from FuncIndex ");
            List<FuncIndex> funcIndices = query.getResultList();
            return funcIndices;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
