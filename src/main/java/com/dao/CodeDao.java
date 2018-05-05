package com.dao;

import com.entity.Code;
import com.entity.Doc;
import com.main.Main;
import com.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Set;

/**
 * Created by flyboss on 2018/4/4.
 */
public class CodeDao {
    private final static Logger logger = LogManager.getLogger(CodeDao.class);

    public Boolean add(Code code) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(code);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(code.getFullName() + "\n" + code.getOriginBody().length());
            logger.error(code.getOriginBody());
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public Code find(Integer id) {
        Session session = null;
        try {
            session = Main.sessionFactory.openSession();
            Code code = session.find(Code.class, id);
            Set<Doc> set = code.getDocs();
            return code;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void update(Code code) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(code);
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

    public void delete(Integer id) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            Code code = new Code();
            code.setId(id);
            session.delete(code);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    public long getCodeCount() {
        Session session = null;
        String hql="select count(*) FROM Code " ;
        try {
            session = Main.sessionFactory.openSession();
            Query query = session.createQuery(hql);
            long count=(long)query.getResultList().get(0);
            return count;
        } catch (Exception e) {
            return 10000000000L;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
