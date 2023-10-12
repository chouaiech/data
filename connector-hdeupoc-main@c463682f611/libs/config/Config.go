package config

import (
	"fmt"
	"os"

	"github.com/kelseyhightower/envconfig"
	"gopkg.in/yaml.v2"
)

type Config struct {
	Domibus struct {
		Username    string `yaml:username`
		WspluginUrl string `yaml:wspluginurl`
		BackendUrl  string `yaml:backendurl`
		Url         string `yaml:url`
		Port        string `yaml:port`
		From        string `yaml:from`
		To          string `yaml:to`
		UseHttps    bool   `yaml:usehttps,omitempty`
	} `yaml:"domibus"`
	Fairdatapoint struct {
		UseHttps bool   `yaml:usehttps,omitempty`
		Url      string `yaml:url`
		Username string `yaml:username`
		Port 	 string `yaml:port`
	} `yaml:"fairdatapoint"`
	MessageChecker struct {
		Port        string `yaml:port`
		Url         string `yaml:url`
		UseHttps    bool   `default:"false" yaml:usehttps`
		KeyFileName string `default:"localhost.key" yaml:keyfilename`
		CrtFileName string `default:"localhost.crt" yaml:crtfilename`
	} `yaml:"messagechecker"`
	DataDiscoveryCentralService struct {
		Port string `yaml:port`
		Url  string `yaml:url`
	} `yaml:"datadiscoverycentralservice"`
	Gateway struct {
		Port            string `yaml:port`
		UseHttps        bool   `default:"false" yaml:usehttps`
		KeyFileName     string `default:"localhost.key" yaml:keyfilename`
		CrtFileName     string `default:"localhost.crt" yaml:crtfilename`
		MessageListSize int    `default:"100" yaml:"message_list_size"`
	} `yaml:"gateway"`
	DataPermit struct {
		Port        string `yaml:port`
		Url         string `yaml:url`
		UseHttps    bool   `default:"false" yaml:usehttps`
		KeyFileName string `default:"localhost.key" yaml:keyfilename`
		CrtFileName string `default:"localhost.crt" yaml:crtfilename`
	} `yaml:"datapermit"`
	DataPermitCentralService struct {
		Port            string `yaml:port`
		Url             string `yaml:url`
		MessageListSize int    `default:"100" yaml:"message_list_size"`
	} `yaml:"datapermitcentralservice"`
}

func ReadConfig() *Config {
	var configFile string
	if len(os.Args) > 1 {
		configFile = os.Args[1]
	} else {
		configFile = "config.yml"
	}
	fmt.Printf("[LOG] Using config:%s\n", configFile)
	conf, err := ReadConfigFile(configFile)
	if err != nil {
		fmt.Printf("[ERROR] reading config:%s, %s\n", configFile, err)
		os.Exit(2)
	}
	return conf
}

func ReadConfigFile(filename string) (*Config, error) {
	var config Config
	f, err := os.ReadFile(filename)
	if err != nil {
		return nil, err
	}

	err = yaml.Unmarshal(f, &config)
	if err != nil {
		return nil, err
	}
	return &config, nil
}

func ReadEnv() (*Config, error) {
	var config Config
	err := envconfig.Process("", config)
	if err != nil {
		return nil, err
	}
	return &config, nil
}
