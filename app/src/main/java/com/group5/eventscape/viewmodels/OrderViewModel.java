package com.group5.eventscape.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.group5.eventscape.models.Order;
import com.group5.eventscape.repositories.OrderRepository;

import java.util.List;

public class OrderViewModel extends AndroidViewModel {
    private final OrderRepository repository = new OrderRepository();
    private static OrderViewModel instance;

    public MutableLiveData<List<Order>> allOrders;
    public MutableLiveData<List<Order>> userOrders;
    public MutableLiveData<String> generatedOrderId = new MutableLiveData<>();

    public OrderViewModel(@NonNull Application application){
        super(application);
    }

    public static OrderViewModel getInstance(Application application){
        if (instance == null){
            instance = new OrderViewModel(application);
        }
        return instance;
    }

    public OrderRepository getOrdersRepository(){
        return this.repository;
    }

    public void addOrder(Order order){
        this.repository.addOrder(order);
        this.generatedOrderId = this.repository.generatedOrderId;
    }

    public void getAllOrders(){
        this.repository.getAllOrders();
        this.allOrders = this.repository.allOrders;
    }

    public void getOrdersOfCurUser(String userEmail){
        this.repository.getOrdersOfCurUser(userEmail);
        this.userOrders = this.repository.userOrders;
    }
}
