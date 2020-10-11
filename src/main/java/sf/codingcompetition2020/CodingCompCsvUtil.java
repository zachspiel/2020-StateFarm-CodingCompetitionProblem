package sf.codingcompetition2020;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import sf.codingcompetition2020.structures.Agent;
import sf.codingcompetition2020.structures.Claim;
import sf.codingcompetition2020.structures.Customer;
import sf.codingcompetition2020.structures.Vendor;

public class CodingCompCsvUtil {

	/*
	 * #1 readCsvFile() -- Read in a CSV File and return a list of entries in that
	 * file.
	 * 
	 * @param filePath -- Path to file being read in.
	 * 
	 * @param classType -- Class of entries being read in.
	 * 
	 * @return -- List of entries being returned.
	 */
	public <T> List<T> readCsvFile(String filePath, Class<T> classType) {
		String className = classType.getSimpleName();
		List<T> csvFileEntries = new ArrayList<T>();

		String line = "";
		int index = 0;
		BufferedReader fileScanner;

		try {
			fileScanner = new BufferedReader(new FileReader(filePath));

			while ((line = fileScanner.readLine()) != null) {
				if (index != 0) {
					String[] currentEntry = line.split(",");

					if (className.equals("Agent")) {
						Agent newAgent = new Agent(currentEntry);
						csvFileEntries.add((T) newAgent);
					} else if (className.equals("Claim")) {
						Claim newClaim = new Claim(currentEntry);
						csvFileEntries.add((T) newClaim);
					} else if (className.equals("Customer")) {
						Customer newCustomer = new Customer(currentEntry);
						csvFileEntries.add((T) newCustomer);
					}
				}
				index += 1;
			}

			fileScanner.close();
		} catch (IOException error) {
			error.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}

		return csvFileEntries;
	}

	/*
	 * #2 getAgentCountInArea() -- Return the number of agents in a given area.
	 * 
	 * @param filePath -- Path to file being read in.
	 * 
	 * @param area -- The area from which the agents should be counted.
	 * 
	 * @return -- The number of agents in a given area
	 */
	public int getAgentCountInArea(String filePath, String area) {

		String line = "";
		int index = 0;
		int totalAgents = 0;
		int agentAreaIndex = 1;

		BufferedReader fileScanner;

		try {
			fileScanner = new BufferedReader(new FileReader(filePath));

			while ((line = fileScanner.readLine()) != null) {
				if (index != 0) {
					String[] currentEntry = line.split(",");

					if (currentEntry[agentAreaIndex].equals(area)) {
						totalAgents += 1;
					}
				}
				index += 1;
			}

			fileScanner.close();
		} catch (IOException error) {
			error.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}

		return totalAgents;
	}

	/*
	 * #3 getAgentsInAreaThatSpeakLanguage() -- Return a list of agents from a given
	 * area, that speak a certain language.
	 * 
	 * @param filePath -- Path to file being read in.
	 * 
	 * @param area -- The area from which the agents should be counted.
	 * 
	 * @param language -- The language spoken by the agent(s).
	 * 
	 * @return -- The number of agents in a given area
	 */
	public List<Agent> getAgentsInAreaThatSpeakLanguage(String filePath, String area, String language) {
		List<Agent> agentsThatSpeakLangauge = new ArrayList<Agent>();
		String line = "";
		int index = 0;
		int agentAreaIndex = 1;
		int agentLanguageIndex = 2;

		BufferedReader fileScanner;

		try {
			fileScanner = new BufferedReader(new FileReader(filePath));

			while ((line = fileScanner.readLine()) != null) {
				if (index != 0) {
					String[] currentEntry = line.split(",");

					if (currentEntry[agentAreaIndex].equals(area)
							&& currentEntry[agentLanguageIndex].equals(language)) {
						Agent newAgent = new Agent(currentEntry);

						agentsThatSpeakLangauge.add(newAgent);
					}
				}
				index += 1;
			}

			fileScanner.close();
		} catch (IOException error) {
			error.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}

		return agentsThatSpeakLangauge;
	}

	/*
	 * #4 countCustomersFromAreaThatUseAgent() -- Return the number of individuals
	 * from an area that use a certain agent.
	 * 
	 * @param filePath -- Path to file being read in.
	 * 
	 * @param customerArea -- The area from which the customers should be counted.
	 * 
	 * @param agentFirstName -- First name of agent.
	 * 
	 * @param agentLastName -- Last name of agent.
	 * 
	 * @return -- The number of customers that use a certain agent in a given area.
	 */
	public short countCustomersFromAreaThatUseAgent(Map<String, String> csvFilePaths, String customerArea,
			String agentFirstName, String agentLastName) {

		int totalCustomersFromAreaThatUseAgent = 0;
		String line = "";
		int index = 0;

		BufferedReader fileScanner;
		List<Agent> agents = new ArrayList<Agent>();

		try {
			String agentFilePath = (String) csvFilePaths.get("agentList");
			String customerFilePath = (String) csvFilePaths.get("customerList");

			fileScanner = new BufferedReader(new FileReader(agentFilePath));

			while ((line = fileScanner.readLine()) != null) {
				if (index != 0) {

					String[] currentEntry = line.split(",");

					Agent newAgent = new Agent(currentEntry);

					agents.add(newAgent);
				}

				index += 1;
			}
			index = 0;
			fileScanner.close();
			fileScanner = new BufferedReader(new FileReader(customerFilePath));

			while ((line = fileScanner.readLine()) != null) {
				if (index != 0) {
					String[] currentEntry = line.split(",");

					int customerAgentId = Integer.parseInt(currentEntry[5]);

					Agent customerAgent = agents.get(customerAgentId);

					if (customerAgent.getFirstName().equals(agentFirstName)
							&& customerAgent.getLastName().equals(agentLastName)
							&& currentEntry[4].equals(customerArea)) {
						totalCustomersFromAreaThatUseAgent += 1;
					}

				}
				index += 1;
			}
			fileScanner.close();

		} catch (IOException error) {
			error.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return (short) totalCustomersFromAreaThatUseAgent;

	}

	/*
	 * #5 getCustomersRetainedForYearsByPlcyCostAsc() -- Return a list of customers
	 * retained for a given number of years, in ascending order of their policy
	 * cost.
	 * 
	 * @param filePath -- Path to file being read in.
	 * 
	 * @param yearsOfServeice -- Number of years the person has been a customer.
	 * 
	 * @return -- List of customers retained for a given number of years, in
	 * ascending order of policy cost.
	 */
	public List<Customer> getCustomersRetainedForYearsByPlcyCostAsc(String customerFilePath, short yearsOfService) {

		List<Customer> customersRetainedForYears = new ArrayList<Customer>();
		String line = "";
		int index = 0;

		BufferedReader fileScanner;

		try {
			fileScanner = new BufferedReader(new FileReader(customerFilePath));

			while ((line = fileScanner.readLine()) != null) {
				if (index != 0) {
					String[] currentEntry = line.split(",");
					Short customerYearsOfService = Short.parseShort(currentEntry[13]);
					if (customerYearsOfService == yearsOfService) {
						Customer newCustomer = new Customer(currentEntry);

						customersRetainedForYears.add(newCustomer);
					}
				}
				index += 1;
			}

			fileScanner.close();
		} catch (IOException error) {
			error.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}

		Collections.sort(customersRetainedForYears, new Comparator<Customer>() {
			public int compare(Customer customer1, Customer customer2) {
				return customer1.getYearsOfService() - customer2.getYearsOfService();
			}

		});
		return customersRetainedForYears;

	}

	/*
	 * #6 getLeadsForInsurance() -- Return a list of individuals who’ve made an
	 * inquiry for a policy but have not signed up. *HINT* -- Look for customers
	 * that currently have no policies with the insurance company.
	 * 
	 * @param filePath -- Path to file being read in.
	 * 
	 * @return -- List of customers who’ve made an inquiry for a policy but have not
	 * signed up.
	 */
	public List<Customer> getLeadsForInsurance(String filePath) {
		List<Customer> customerLeads = new ArrayList<Customer>();
		String line = "";
		int index = 0;

		BufferedReader fileScanner;

		try {
			fileScanner = new BufferedReader(new FileReader(filePath));

			while ((line = fileScanner.readLine()) != null) {
				if (index != 0) {
					String[] currentEntry = line.split(",");

					Customer newCustomer = new Customer(currentEntry);

					if (!newCustomer.getAutoPolicy() && !newCustomer.getHomePolicy()
							&& !newCustomer.getRentersPolicy()) {
						customerLeads.add(newCustomer);
					}
				}
				index += 1;
			}

			fileScanner.close();
		} catch (IOException error) {
			error.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}

		return customerLeads;

	}

	/*
	 * #7 getVendorsWithGivenRatingThatAreInScope() -- Return a list of vendors
	 * within an area and include options to narrow it down by: a. Vendor rating b.
	 * Whether that vendor is in scope of the insurance (if inScope == false, return
	 * all vendors in OR out of scope, if inScope == true, return ONLY vendors in
	 * scope)
	 * 
	 * @param filePath -- Path to file being read in.
	 * 
	 * @param area -- Area of the vendor.
	 * 
	 * @param inScope -- Whether or not the vendor is in scope of the insurance.
	 * 
	 * @param vendorRating -- The rating of the vendor.
	 * 
	 * @return -- List of vendors within a given area, filtered by scope and vendor
	 * rating.
	 */
	public List<Vendor> getVendorsWithGivenRatingThatAreInScope(String filePath, String area, boolean inScope,
			int vendorRating) {
		return null;

	}

	/*
	 * #8 getUndisclosedDrivers() -- Return a list of customers between the age of
	 * 40 and 50 years (inclusive), who have: a. More than X cars b. less than or
	 * equal to X number of dependents.
	 * 
	 * @param filePath -- Path to file being read in.
	 * 
	 * @param vehiclesInsured -- The number of vehicles insured.
	 * 
	 * @param dependents -- The number of dependents on the insurance policy.
	 * 
	 * @return -- List of customers filtered by age, number of vehicles insured and
	 * the number of dependents.
	 */
	public List<Customer> getUndisclosedDrivers(String filePath, int vehiclesInsured, int dependents) {
		return null;

	}

	/*
	 * #9 getAgentIdGivenRank() -- Return the agent with the given rank based on
	 * average customer satisfaction rating. *HINT* -- Rating is calculated by
	 * taking all the agent rating by customers (1-5 scale) and dividing by the
	 * total number of reviews for the agent.
	 * 
	 * @param filePath -- Path to file being read in.
	 * 
	 * @param agentRank -- The rank of the agent being requested.
	 * 
	 * @return -- Agent ID of agent with the given rank.
	 */
	public int getAgentIdGivenRank(String filePath, int agentRank) {
		return agentRank;

	}

	/*
	 * #10 getCustomersWithClaims() -- Return a list of customers who’ve filed a
	 * claim within the last <numberOfMonths> (inclusive).
	 * 
	 * @param filePath -- Path to file being read in.
	 * 
	 * @param monthsOpen -- Number of months a policy has been open.
	 * 
	 * @return -- List of customers who’ve filed a claim within the last
	 * <numberOfMonths>.
	 */
	public List<Customer> getCustomersWithClaims(Map<String, String> csvFilePaths, short monthsOpen) {
		return null;

	}

}
