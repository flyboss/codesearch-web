package com.dao;

import com.entity.ApiIndexName;
import com.entity.ApiIndexNameDoc;
import com.entity.Doc;
import com.main.Main;
import com.util.HibernateUtil;
import org.apache.xpath.SourceTree;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Map;

/**
 * Created by flyboss on 2018/4/20.
 */
public class ApiIndexNameDocDao{
    public ApiIndexNameDoc add(ApiIndexNameDoc apiIndexNameDoc) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(apiIndexNameDoc);
            transaction.commit();
            return apiIndexNameDoc;
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

    public List<Object[]> countIDF(){
        Session session = null;
        String hql="select apiIndexName.id,count(*) FROM ApiIndexNameDoc group by apiIndexName" ;
        try {
            session = Main.sessionFactory.openSession();
            Query query = session.createQuery(hql);
            List<Object[]> objects = query.getResultList();
            return objects;
        } catch (Exception e) {
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<ApiIndexNameDoc> findByDocId(int id) {
        Session session = null;
        String hql="FROM ApiIndexNameDoc where doc.id=:id" ;
        try {
            session = Main.sessionFactory.openSession();
            Query query = session.createQuery(hql);
            query.setParameter("id",id);
            List<ApiIndexNameDoc> objects = query.getResultList();
            return objects;
        } catch (Exception e) {
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<ApiIndexNameDoc> findByNameId(int id){
        Session session = null;
        String hql="FROM ApiIndexNameDoc where apiIndexName.id=:id" ;
        try {
            session = Main.sessionFactory.openSession();
            Query query = session.createQuery(hql);
            query.setParameter("id",id);
            List<ApiIndexNameDoc> objects = query.getResultList();
            return objects;
        } catch (Exception e) {
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

}
