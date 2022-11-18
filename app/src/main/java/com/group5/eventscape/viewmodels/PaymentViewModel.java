package com.group5.eventscape.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.group5.eventscape.models.Payment;
import com.group5.eventscape.repositories.PaymentRepository;

import java.util.List;

public class PaymentViewModel extends AndroidViewModel {

    private final PaymentRepository repository = new PaymentRepository();
    private static PaymentViewModel instance;

    public MutableLiveData<List<Payment>> allPayments = new MutableLiveData<>();

    public PaymentViewModel(@NonNull Application application) {
        super(application);
    }

    public static PaymentViewModel getInstance(Application application) {
        if (instance == null) {
            instance = new PaymentViewModel(application);
        }
        return instance;
    }

    public void addPayment(Payment payment) {
        this.repository.addPayment(payment);
    }

    public void getPayments() {
        this.repository.getPayments();
        this.allPayments = this.repository.allPayments;
    }
}
