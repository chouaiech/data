package main

import (
	"bufio"
	"fmt"
	"os"

	"code.europa.eu/healthdataeu-nodes/hdeupoc/libs/web/apikey"
)

func main() {
	fmt.Println("Please tape a name for your API Key:")

	scanner := bufio.NewScanner(os.Stdin)
	scanner.Scan()

	input := scanner.Text()

	k, err := apikey.New(input)
	if err != nil {
		fmt.Println(err)
		return
	}

	fmt.Println("!!!WARNING: PLEASE RECORD YOUR API KEY!!!")
	fmt.Printf("Your API key is: %q\n", k.Code)

	root, err := os.Getwd()
	if err != nil {
		fmt.Println(err)
		return
	}

	dir := root + "/keys"

	if err := os.MkdirAll(dir, os.ModePerm); err != nil {
		fmt.Println(err)
		return
	}

	path := dir + "/apikeys.json"

	err = apikey.Add(k, path)
	if err != nil {
		fmt.Println(err)
	}
}
