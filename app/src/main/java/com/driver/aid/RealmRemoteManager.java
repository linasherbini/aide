package com.driver.aid;


import com.driver.aid.Model.Driver;
import com.driver.aid.Model.Employee;
import com.driver.aid.Model.Feedback;
import com.driver.aid.Model.Order;
import com.driver.aid.Model.Shop;
import com.driver.aid.Model.Content;


import java.util.List;

import io.reactivex.Observable;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.SyncConfiguration;
import io.realm.SyncUser;

import static com.driver.aid.Model.Order.STATUS_PENDING;
import static com.driver.aid.UserType.DRIVER;
import static com.driver.aid.UserType.EMPLOYEE;
import static com.driver.aid.UserType.SHOP;

public class RealmRemoteManager {
    private static final String DEFAULT_DATABAASE = "realms://driveraid.us1a.cloud.realm.io/default";
    private Realm realm;

    public static RealmRemoteManager getInstance() {
        return instance;
    }

    public static RealmRemoteManager instance;
    private final SyncUser syncUser;

    private RealmRemoteManager(SyncUser syncUser) {
        this.syncUser = syncUser;
        SyncConfiguration config = syncUser.createConfiguration(DEFAULT_DATABAASE)
                .schemaVersion(2)
                .build();
        realm = Realm.getInstance(config);
    }

    public static void init(SyncUser syncUser) {
        instance = new RealmRemoteManager(syncUser);

    }


    public void updateDriverInfo(Driver driver) {
        Realm realm = getRealm();
        realm.executeTransactionAsync(realm1 -> realm1.insert(driver));
    }

    public Realm getRealm() {

        return realm;
    }

    public void updateOrder(String orderId, String technicianName, String technicianNumber, String shopId, String shopName, String status) {
        Realm realm = getRealm();
        realm.executeTransactionAsync(realm1 -> {
            Order resultOrder = realm1.where(Order.class).equalTo("orderId", orderId).findFirst();
            if (resultOrder != null) {
                resultOrder.setTechnicianName(technicianName);
                resultOrder.setTechnicianNumber(technicianNumber);
                resultOrder.setWorkshopId(shopId);
                resultOrder.setWorkshopName(shopName);
                resultOrder.setStatus(status);
            }

        });
    }

    public void updateOrderStatus(String orderId, String status) {
        Realm realm = getRealm();
        realm.executeTransactionAsync(realm1 -> {
            Order resultOrder = realm1.where(Order.class).equalTo("orderId", orderId).findFirst();
            if (resultOrder != null) {
                resultOrder.setStatus(status);
            }

        });
    }

    public void logout() {
        syncUser.logOut();
        realm.close();
    }

    public void updateDriverInfo(String fullname, String nationalId, String driverPhone, String carType, String plateNumber) {
        Realm realm = getRealm();
        realm.executeTransactionAsync(realm1 -> {
            Driver driver = realm1.where(Driver.class).equalTo("userId", syncUser.getIdentity()).findFirst();
            if (driver != null) {
                driver.setFullName(fullname);
                driver.setNationalID(nationalId);
                driver.setPhone(driverPhone);
                driver.setCarType(carType);
                driver.setPlateNumber(plateNumber);
            }

        });

    }

    public void updateDriverEmail(String email) {
        Realm realm = getRealm();
        realm.executeTransactionAsync(realm1 -> {
            Driver driver = realm1.where(Driver.class).equalTo("userId", syncUser.getIdentity()).findFirst();
            if (driver != null) {
                driver.setEmail(email);
            }

        });
    }

    public void submitFeedback(Feedback feedbackObject) {
        Realm realm = getRealm();
        realm.executeTransactionAsync(realm1 -> {
            realm1.insert(feedbackObject);

        });
    }

    public void updateOrderCost(String orderId, String cost, String status) {
        Realm realm = getRealm();
        realm.executeTransactionAsync(realm1 -> {
            Order resultOrder = realm1.where(Order.class).equalTo("orderId", orderId).findFirst();
            if (resultOrder != null) {
                resultOrder.setStatus(status);
                resultOrder.setPrice(cost);
            }

        });
    }

    interface QueryUserType {
        void onUserType(UserType userType);
    }

    public interface DriverListener{
        public void result(Driver driver);
    }

    public RealmResults<Driver> getCurrentDriver(DriverListener driverListener) {
        RealmResults<Driver> result = getRealm().where(Driver.class).equalTo("userId", syncUser.getIdentity())
                .findAllAsync();
        OrderedRealmCollectionChangeListener<RealmResults<Driver>> listener = (drivers, changeSet) -> driverListener.result(drivers.get(0));
        result.addChangeListener(listener);
        return result;
    }

    public void getCurrentUserType(QueryUserType queryUserType) {
        Realm realm = getRealm();

        RealmResults<Driver> driverResult = realm.where(Driver.class).equalTo("userId", syncUser.getIdentity()).findAllAsync();
        OrderedRealmCollectionChangeListener<RealmResults<Driver>> driverListenr = (drivers, changeSet) -> {
            if (drivers.size() > 0) {
                queryUserType.onUserType(DRIVER);
                driverResult.removeAllChangeListeners();
            }
        };

        driverResult.addChangeListener(driverListenr);


        RealmResults<Shop> shopResult = realm.where(Shop.class).equalTo("userId", syncUser.getIdentity()).findAllAsync();
        OrderedRealmCollectionChangeListener<RealmResults<Shop>> shopListenr = new OrderedRealmCollectionChangeListener<RealmResults<Shop>>() {
            @Override
            public void onChange(RealmResults<Shop> drivers, OrderedCollectionChangeSet changeSet) {
                if (drivers.size() > 0) {
                    queryUserType.onUserType(SHOP);
                    shopResult.removeAllChangeListeners();
                }
            }
        };

        shopResult.addChangeListener(shopListenr);


        RealmResults<Employee> employeeRealmResults = realm.where(Employee.class).equalTo("userId", syncUser.getIdentity()).findAllAsync();
        OrderedRealmCollectionChangeListener<RealmResults<Employee>> empListener = new OrderedRealmCollectionChangeListener<RealmResults<Employee>>() {
            @Override
            public void onChange(RealmResults<Employee> employees, OrderedCollectionChangeSet changeSet) {
                if (employees.size() > 0) {
                    queryUserType.onUserType(EMPLOYEE);
                    shopResult.removeAllChangeListeners();
                }
            }
        };

        employeeRealmResults.addChangeListener(empListener);


    }

    public void updateShopInfo(Shop shop) {
        Realm realm = getRealm();
        realm.executeTransactionAsync(realm1 -> {
            realm1.insert(shop);

        });
    }

    public void updateEmployee(Employee employee) {
        Realm realm = getRealm();
        realm.executeTransactionAsync(realm1 -> realm1.insert(employee));
    }


    interface UpdateLoggedInUser {
        void onUserUpdated(UserType userType);
    }

    public void updateLoggedInUser(UpdateLoggedInUser updateLoggedInUserListener) {

        getCurrentUserType(userType -> {
            switch (userType) {
                case DRIVER: {

                    RealmResults<Driver> driverResult = realm.where(Driver.class).equalTo("userId", syncUser.getIdentity()).findAllAsync();
                    OrderedRealmCollectionChangeListener<RealmResults<Driver>> driverListenr = (drivers, changeSet) -> {
                        if (drivers.size() > 0) {
                            loginDriver(userType, drivers.get(0));
                            driverResult.removeAllChangeListeners();
                            updateLoggedInUserListener.onUserUpdated(userType);
                        }
                    };
                    driverResult.addChangeListener(driverListenr);

                }
                case SHOP: {
                    RealmResults<Shop> shopRealmResults = realm.where(Shop.class).equalTo("userId", syncUser.getIdentity()).findAllAsync();
                    OrderedRealmCollectionChangeListener<RealmResults<Shop>> driverListenr = (shops, changeSet) -> {
                        if (shops.size() > 0) {
                            loginShop(userType, shops.get(0));
                            shopRealmResults.removeAllChangeListeners();
                            updateLoggedInUserListener.onUserUpdated(userType);
                        }
                    };
                    shopRealmResults.addChangeListener(driverListenr);
                }
                case EMPLOYEE: {
                    RealmResults<Employee> shopRealmResults = realm.where(Employee.class).equalTo("userId", syncUser.getIdentity()).findAllAsync();
                    OrderedRealmCollectionChangeListener<RealmResults<Employee>> driverListenr = (employee, changeSet) -> {
                        if (employee.size() > 0) {
                            loginShop(userType, employee.get(0));
                            shopRealmResults.removeAllChangeListeners();
                            updateLoggedInUserListener.onUserUpdated(userType);
                        }
                    };
                    shopRealmResults.addChangeListener(driverListenr);
                }
            }
        });
    }

    private UserType loginShop(UserType userType, Shop shop) {
        LoggedInUserManager.init(userType, shop);
        return userType;
    }

    private UserType loginShop(UserType userType, Employee employee) {
        LoggedInUserManager.init(userType, employee);
        return userType;
    }

    private Observable<Shop> getShop(String identity) {
        return Observable.fromCallable(() -> getRealm().where(Shop.class).equalTo("userId", identity).findFirst());
    }

    private UserType loginDriver(UserType userType, Driver driver) {
        LoggedInUserManager.init(userType, driver);
        return userType;
    }

    private Observable<Driver> getDriver(String identity) {
        return Observable.fromCallable(() -> getRealm().where(Driver.class).equalTo("userId", identity).findFirst());
    }

    public void submitOrder(Order order) {
        getRealm().executeTransactionAsync(realm1 -> {
            realm1.insert(order);
        });
    }

    public interface StatusRepair {
        void result(List<Order> orders);
    }

    public RealmResults<Order> getRepairRequestStatus(StatusRepair statusRepair) {
        RealmResults<Order> result = getRealm().where(Order.class).equalTo("driverID", syncUser.getIdentity())
                .findAllAsync();
        OrderedRealmCollectionChangeListener<RealmResults<Order>> listener = (orders, changeSet) -> {
            statusRepair.result(orders);

        };

        result.addChangeListener(listener);
        return result;
    }

    public interface ShopRequests {
        void result(List<Order> currentRequest, List<Order> newRequests);
    }

    public RealmResults<Order> getRepairShopRequests(String repairShopId, ShopRequests shopRequests) {

        RealmResults<Order> result = getRealm().where(Order.class).equalTo("status", STATUS_PENDING)
                .or().equalTo("workshopId", repairShopId)
                .findAllAsync();
        OrderedRealmCollectionChangeListener<RealmResults<Order>> listener = (orders, changeSet) -> {
            shopRequests.result(orders.where().equalTo("workshopId", repairShopId).findAll(), orders.where().equalTo("status", STATUS_PENDING).findAll());
        };

        result.addChangeListener(listener);
        return result;
    }

    public interface ContentResultListener {
        void result(List<Content> contentList);
    }

    public RealmResults<Content> getContent(ContentResultListener contentResultListener, String type) {
        RealmResults<Content> result = getRealm().where(Content.class).equalTo("type", type)
                .findAllAsync();
        OrderedRealmCollectionChangeListener<RealmResults<Content>> listener = (signesList, changeSet) -> contentResultListener.result(signesList);

        result.addChangeListener(listener);
        return result;
    }


}
