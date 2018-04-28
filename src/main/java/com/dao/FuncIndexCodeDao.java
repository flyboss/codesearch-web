package com.dao;

import com.entity.Code;
import com.entity.FuncIndex;
import com.entity.FuncIndexCode;
import com.main.Main;
import com.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by flyboss on 2018/4/25.
 */
public class FuncIndexCodeDao {
    public boolean add(FuncIndexCode funcIndexCode) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(funcIndexCode);
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

    public FuncIndexCode findByCodeAndFuncIndex(int codeId,int funcIndexId,boolean isName){
        Session session = null;
        try {
            session = Main.sessionFactory.openSession();
            Query query = session.createQuery("from FuncIndexCode where code.id=:codeId and funcIndex.id=:funcIndexId and isName=:isName");
            query.setParameter("codeId", codeId);
            query.setParameter("funcIndexId", funcIndexId);
            query.setParameter("isName", isName);
            List<FuncIndexCode> funcIndexCodes = query.getResultList();
            if (funcIndexCodes.size() == 0) {
                return null;
            } else {
                return funcIndexCodes.get(0);
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
