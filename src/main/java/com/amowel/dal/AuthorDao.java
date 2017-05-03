package com.amowel.dal;

import com.amowel.model.Author;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by amowel on 30.04.17.
 */
@Repository
@Transactional
public class AuthorDao implements CrudRepository<Author> {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Author> findAll() {
        return sessionFactory
                .getCurrentSession()
                .createQuery("from Author ")
                .list();
    }

    @Override
    public Author findById(Long id) {
        return (Author) sessionFactory
                .getCurrentSession()
                .createCriteria(Author.class)
                .add(Restrictions.eq("id", id))
                .addOrder(Order.desc("name"))
                .uniqueResult();
    }

    @Override
    public Author saveOrUpdate(Author author) {
        sessionFactory
                .getCurrentSession()
                .saveOrUpdate(author);
        return author;
    }

    @Override
    public Author delete(Author author) {
        sessionFactory
                .getCurrentSession()
                .delete(author);
        return author;
    }

    public Author findByName(String name) {
        return (Author) sessionFactory
                .getCurrentSession()
                .createCriteria(Author.class)
                .add(Restrictions.eq("name", name))
                .uniqueResult();
    }
}
