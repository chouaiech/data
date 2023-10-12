package repository

import (
	"database/sql"
	"encoding/json"
	"fmt"
	"os"
)

type DataPermitRepositoryInterface interface {
	CreateTableIfNotExists()
	GetAllDataPermits() ([]byte, error)
	AddDataPermit(json_form string) error
	CloseDb()
}

type DataPermitRepository struct {
	Db *sql.DB
}

type JsonResult struct {
	Array []string
}

type DataPermit struct {
	Id         string
	Status     string
	Datapermit json.RawMessage
}

func openDb() *sql.DB {

	db, err := sql.Open("sqlite3", "./db/nc.db")
	if err != nil {
		fmt.Printf("[ERROR] %v\n", err)
		os.Exit(3)
	}
	return db
}

func (repository DataPermitRepository) CloseDb() {
	repository.Db.Close()
}

func CreateDataPermitRepository() *DataPermitRepository {
	dataPermitRepository := DataPermitRepository{
		Db: openDb(),
	}
	return &dataPermitRepository
}

func (repository DataPermitRepository) CreateTableIfNotExists() {
	sqlStmt := `CREATE TABLE IF NOT EXISTS datapermit (id INTEGER PRIMARY KEY AUTOINCREMENT,
		status text NOT NULL,
		data_permit json NOT NULL);`

	_, err := repository.Db.Exec(sqlStmt)
	if err != nil {
		fmt.Printf("[ERROR] %q: %s\n", err, sqlStmt)
		return
	}
}

func (repository DataPermitRepository) GetAllDataPermits() ([]byte, error) {
	rows, err := repository.Db.Query("select id, status, data_permit from datapermit")
	if err != nil {
		fmt.Printf("[ERROR] Repository GetAllDataPermits: %v\n", err)
	}
	defer rows.Close()
	var a []DataPermit
	for rows.Next() {
		var id int
		var status string
		var datapermit string
		err = rows.Scan(&id, &status, &datapermit)
		if err != nil {
			fmt.Printf("[ERROR] DataPermitService rows.Scan: %v\n", err)
			return nil, err
		}
		datapermitJson := json.RawMessage(datapermit)
		datapermitJsonMarshal, err := json.Marshal(&datapermitJson)
		if err != nil {
			fmt.Printf("[ERROR] DataPermitService json.Marshal: %v\n", err)
			return nil, err
		}

		dataPermit := DataPermit{
			Id:         fmt.Sprint(id),
			Status:     status,
			Datapermit: datapermitJsonMarshal,
		}
		a = append(a, dataPermit)

	}
	err = rows.Err()
	if err != nil {
		fmt.Printf("[ERROR] DataPermitService rows.Err: %v\n", err)
		return nil, err
	}
	return json.Marshal(a)
}

func (repository DataPermitRepository) AddDataPermit(json_form string) error {
	fmt.Printf("[LOG] DataPermitService Add\n")
	tx, err := repository.Db.Begin()
	if err != nil {
		fmt.Printf("[ERROR] DataPermitService transaction begin: %v\n", err)
		return err
	}
	stmt, err := tx.Prepare("INSERT INTO datapermit (status,data_permit) VALUES ('NEW',json(?))")
	if err != nil {
		fmt.Printf("[ERROR] DataPermitService insert: %v\n", err)
		return err
	}
	defer stmt.Close()
	_, err = stmt.Exec(fmt.Sprint(json_form))
	if err != nil {
		fmt.Printf("[ERROR] DataPermitService exec: %v\n", err)
		return err
	}
	err = tx.Commit()
	if err != nil {
		fmt.Printf("[ERROR] DataPermitService transaction commit: %v\n", err)
		return err
	}
	return nil

}
