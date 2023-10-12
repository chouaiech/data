package services

import (
	"code.europa.eu/healthdataeu-nodes/hdeupoc/services/datapermit/repository"
)

type DataPermitServiceInterface interface {
	GetAllDataPermits() ([]byte, error)
	AddDataPermit(json_form string) error
}

type DataPermitService struct {
	DataPermitRepository repository.DataPermitRepositoryInterface
}

var _ DataPermitServiceInterface = DataPermitService{}

func CreateDataPermitService(dataPermitRepository repository.DataPermitRepositoryInterface) DataPermitService {
	dataPermitService := DataPermitService{
		DataPermitRepository: dataPermitRepository,
	}
	return dataPermitService

}

func (dps DataPermitService) GetAllDataPermits() ([]byte, error) {
	return dps.DataPermitRepository.GetAllDataPermits()
}

func (dps DataPermitService) AddDataPermit(json_form string) error {
	return dps.DataPermitRepository.AddDataPermit(json_form)
}
