package ir.maktabsharif.homeservicephase2.util;

import ir.maktabsharif.homeservicephase2.entity.offer.Offer;
import ir.maktabsharif.homeservicephase2.entity.order.Order;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class CustomDuration {

    public String getDuration(LocalDateTime startTime, LocalDateTime endTime, String type) {
        Duration duration = Duration.between(startTime, endTime);
        StringBuilder stringBuilder = new StringBuilder();
        if (type.equals(Order.class.getName()))
            stringBuilder.append("Requested duration recorded time to do the job: ");
        else if (type.equals(Offer.class.getName()))
            stringBuilder.append("Suggested duration recorded time to do the job: ");
        if (duration.toDaysPart() > 0) {
            stringBuilder.append(duration.toDaysPart());
            if (duration.toDaysPart() > 1)
                stringBuilder.append(" Days : ");
            else stringBuilder.append(" Day : ");
        }
        if (duration.toHoursPart() > 0) {
            stringBuilder.append(duration.toHoursPart());
            if (duration.toHoursPart() > 1)
                stringBuilder.append(" Hours : ");
            else stringBuilder.append(" Hour : ");
        }
        if (duration.toMinutesPart() > 0) {
            stringBuilder.append(duration.toMinutesPart());
            if (duration.toMinutesPart() > 1)
                stringBuilder.append(" Minutes ");
            else stringBuilder.append(" Minute ");
        }
        return stringBuilder.toString();
    }
}
