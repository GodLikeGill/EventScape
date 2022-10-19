package com.group5.eventscape.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.group5.eventscape.models.Orders;
import com.group5.eventscape.repositories.OrdersRepository;

import java.util.List;

public class OrdersViewModel extends AndroidViewModel {
    private final OrdersRepository repository = new OrdersRepository();
    private static OrdersViewModel instance;
    public MutableLiveData<List<Orders>> allOrders;
    public MutableLiveData<List<Orders>> userOrders;

    public OrdersViewModel(@NonNull Application application){
        super(application);
    }


    public static OrdersViewModel getInstance(Application application){
        if (instance == null){
            instance = new OrdersViewModel(application);
        }
        return instance;
    }

    public OrdersRepository getOrdersRepository(){
        return this.repository;
    }

    public void addOrder(Orders order){ this.repository.addOrder(order); }

    public void getAllOrders(){
        this.repository.getAllOrders();
        this.allOrders = this.repository.allOrders;
    }

    public void getOrdersOfCurUser(String userEmail){
        this.repository.getOrdersOfCurUser(userEmail);
        this.userOrders = this.repository.userOrders;
    }


}
