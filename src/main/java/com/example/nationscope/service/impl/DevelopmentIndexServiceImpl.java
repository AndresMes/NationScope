package com.example.nationscope.service.impl;

import com.example.nationscope.client.WorldBankClient;
import com.example.nationscope.dto.external.WorldBankPointDTO;
import com.example.nationscope.dto.response.SocialIndicatorsDTOResponse;
import com.example.nationscope.service.DevelopmentIndexService;
import com.example.nationscope.utils.CountryCodeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DevelopmentIndexServiceImpl implements DevelopmentIndexService {

    private final WorldBankClient worldBankClient;
    private final CountryCodeConverter countryCodeConverter;

    @Override
    public SocialIndicatorsDTOResponse calculateDevelopmentIndexes(String country) {

        String isoCode = countryCodeConverter.convertToIso(country);

        Double literacyRate = safeGetDouble(worldBankClient.getLiteracyRateByCountry(isoCode));
        Double lifeExpectancy = safeGetDouble(worldBankClient.getLifeExpectancyByCountry(isoCode));
        Double povertyRate = safeGetDouble(worldBankClient.getPovertyRateByCountry(isoCode));

        Double schoolLifeExpectancy = safeGetDouble(
                worldBankClient.getSchoolLifeExpetancy(isoCode)
        );

        System.out.println("School Life Expectancy: " + schoolLifeExpectancy);

        Double gniPerCapita = safeGetDouble(
                worldBankClient.getIndicator(isoCode, "NY.GNP.PCAP.PP.KD")
        );

        System.out.println("GNI: " + gniPerCapita);

        Double educationIndex = calculateEducationIndex(schoolLifeExpectancy);

        Double hdi = calculateHDI(lifeExpectancy, educationIndex, gniPerCapita);


        return new SocialIndicatorsDTOResponse(
                literacyRate,
                lifeExpectancy,
                hdi,
                povertyRate,
                educationIndex

        );
    }


    private Double safeGetDouble(WorldBankPointDTO dto) {
        return (dto != null && dto.value() != null) ? dto.value().doubleValue() : null;
    }

    private Double calculateEducationIndex(Double schoolLifeExpectancy) {
        if (schoolLifeExpectancy == null) return null;

        return schoolLifeExpectancy / 18.0;
    }

    private Double calculateHDI(Double lifeExpectancy,
                                Double educationIndex,
                                Double gniPerCapita) {

        if (lifeExpectancy == null ||
                educationIndex == null ||
                gniPerCapita == null) {
            return null;
        }

        if(lifeExpectancy == 0 ||
                educationIndex == 0 ||
                gniPerCapita == 0) {
            return null;
        }

        double healthIndex = (lifeExpectancy - 20.0) / (85.0 - 20.0);

        double incomeIndex =
                (Math.log(gniPerCapita) - Math.log(100)) /
                        (Math.log(75000) - Math.log(100));

        return Math.cbrt(
                healthIndex * educationIndex * incomeIndex
        );
    }
}
