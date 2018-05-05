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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by flyboss on 2018/4/1.
 */
public class DocDao {
    private final static Logger logger = LogManager.getLogger(DocDao.class);
    public boolean add(Doc doc) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(doc);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public Doc find(Integer id) {
        Session session = null;
        try {
            session = Main.sessionFactory.openSession();
            Doc doc = session.find(Doc.class, id);
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public Doc findByFullname(String fullname,String arguments){
        Session session = null;
        try {
            session = Main.sessionFactory.openSession();
            Query query = session.createQuery("from Doc where fullName= :fullname");
            query.setParameter("fullname", fullname);
            List<Doc> docs = query.getResultList();
            for (Doc doc : docs) {
                if(doc.getDocsArgs().equals(arguments)){
                    return doc;
                }
            }
            if(docs.size()==0){
                return null;
            }else {
                return docs.get(0);
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

    public void update(Doc doc) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(doc);
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



    public List<Doc> getAll(){
        Session session = null;
        try {
            session = Main.sessionFactory.openSession();
            Query query = session.createQuery("from Doc");
            List<Doc> docs = query.getResultList();
            return docs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Integer> getAllId(){
        Session session = null;
        try {
            session = Main.sessionFactory.openSession();
            Query query = session.createQuery("select id from Doc");
            List<Integer> docsId = query.getResultList();
            return docsId;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Code> findCodeByDocId(int id) {
        Session session = null;
        try {
            session = Main.sessionFactory.openSession();
            Query query = session.createQuery("from Doc where id =:id");
            query.setParameter("id", id);
            List<Doc> docs = query.getResultList();
            if (docs!=null){
                Doc doc=docs.get(0);
                Set<Code> codes=doc.getCodes();
                List<Code> codeList = new ArrayList<>();
                for (Code c:codes) {
                    codeList.add(c);
                }
                return codeList;
            }else {
                return null;
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
}
