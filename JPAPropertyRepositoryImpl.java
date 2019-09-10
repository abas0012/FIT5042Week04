package fit5042.tutex.repository;

import fit5042.tutex.repository.entities.ContactPerson;
import fit5042.tutex.repository.entities.Property;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Adhi Baskoro
 */
@Stateless
public class JPAPropertyRepositoryImpl implements PropertyRepository {

    @PersistenceContext //@PersistenceContext annotations used to  link EntityManager to a persistence unit
    private EntityManager entityManager;

    @Override
    public void addProperty(Property property) throws Exception {
        List<Property> properties = entityManager.createNamedQuery(Property.GET_ALL_QUERY_NAME).getResultList();
        property.setPropertyId(properties.get(0).getPropertyId() + 1);
        entityManager.persist(property);
    }

    @Override
    public Property searchPropertyById(int id) throws Exception {
        Property property = entityManager.find(Property.class, id);
        property.getTags();
        return property;
    }

    @Override
    public List<Property> getAllProperties() throws Exception {
        return entityManager.createNamedQuery(Property.GET_ALL_QUERY_NAME).getResultList();
    }

    @Override
    public Set<Property> searchPropertyByContactPerson(ContactPerson contactPerson) throws Exception {
        contactPerson = entityManager.find(ContactPerson.class, contactPerson.getConactPersonId());
        contactPerson.getProperties().size();
        entityManager.refresh(contactPerson);

        return contactPerson.getProperties();
    }

    @Override
    public List<ContactPerson> getAllContactPeople() throws Exception {
        return entityManager.createNamedQuery(ContactPerson.GET_ALL_QUERY_NAME).getResultList();
    }

    @Override
    public void removeProperty(int propertyId) throws Exception {
        Property p = searchPropertyById(propertyId); //local variable for Property class
        entityManager.remove(p);
        
        
        //complete this method
    }

    @Override
    public void editProperty(Property property) throws Exception {
        try {
            entityManager.merge(property);
        } catch (Exception ex) {

        }
    }

    @Override
    public List<Property> searchPropertyByBudget(double budget) throws Exception {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder(); //declare a criteria builder first
        CriteriaQuery<Property> q = cb.createQuery(Property.class); //declare the type of the query
        Root<Property> r = q.from(Property.class);  //find the query from which table. Similar to SELECT statement
        Predicate condition = cb.le(r.get("price"), budget); //WHERE our table is less than or equal to "price" column
        q.where(condition);
        TypedQuery<Property> tq = entityManager.createQuery(q); //create the query
        List<Property> result = tq.getResultList();
        return result;
        //complete this method using Criteria API
    }
}
