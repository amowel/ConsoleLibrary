package com.amowel.dal;

import com.amowel.model.Book;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by amowel on 01.05.17.
 */
@Repository
@Transactional
public class BookDao implements CrudRepository<Book> {
    @Autowired
    SessionFactory sessionFactory;
    @Override
    public List<Book> findAll() {
        return sessionFactory
                .getCurrentSession()
                .createCriteria(Book.class)
                .addOrder(Order.desc("title"))
                .list();
    }

    @Override
    public Book findById(Long id) {
        return (Book) sessionFactory
                .getCurrentSession()
                .createCriteria(Book.class)
                .add(Restrictions.eq("id",id))
                .uniqueResult();
    }

    @Override
    public Book saveOrUpdate(Book book) {
        sessionFactory
                .getCurrentSession()
                .saveOrUpdate(book);
        return book;
    }

    @Override
    public Book delete(Book book) {
        sessionFactory
                .getCurrentSession()
                .delete(book);
        return book;
    }
    public List<Book> findByTitle(String title){
        return sessionFactory
                .getCurrentSession()
                .createCriteria(Book.class)
                .add(Restrictions.eq("title", title))
                .list();
    }
}
