package interfaces;

import models.Report;

public interface AdminService {
    void buyBoost(String userId);
    Report generateReport();
}
