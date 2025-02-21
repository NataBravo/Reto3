package com.devsenior.nata;


import java.util.List;

import com.devsenior.nata.model.Loan;

public interface LoanRepository {
    void save(Loan loan);
    List<Loan> findLoanByUserId(String UserId);
    
}
