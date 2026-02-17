/**
 * Simple Online Voting System - Backend
 * A basic implementation for managing candidates and votes
 */

// In-memory database for candidates and votes
const votingDatabase = {
  candidates: [],
  votes: [],
  voters: new Set(), // To track who has already voted
};

/**
 * Candidate class to represent election candidates
 */
class Candidate {
  constructor(id, name, description = "") {
    this.id = id;
    this.name = name;
    this.description = description;
    this.voteCount = 0;
  }

  addVote() {
    this.voteCount++;
  }

  getVotePercentage(totalVotes) {
    return totalVotes === 0 ? 0 : ((this.voteCount / totalVotes) * 100).toFixed(2);
  }
}

/**
 * Vote class to represent individual votes
 */
class Vote {
  constructor(voterId, candidateId, timestamp = new Date()) {
    this.voterId = voterId;
    this.candidateId = candidateId;
    this.timestamp = timestamp;
  }
}

/**
 * Voting System Manager
 */
class VotingSystem {
  constructor() {
    this.database = votingDatabase;
  }

  /**
   * Add a new candidate to the election
   * @param {string} name - Candidate name
   * @param {string} description - Candidate description
   * @returns {object} Created candidate object
   */
  addCandidate(name, description = "") {
    if (!name || name.trim() === "") {
      return { success: false, error: "Candidate name is required" };
    }

    const candidateId = `candidate_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
    const candidate = new Candidate(candidateId, name, description);
    this.database.candidates.push(candidate);

    return {
      success: true,
      message: `Candidate ${name} added successfully`,
      candidate: candidate,
    };
  }

  /**
   * Get all candidates
   * @returns {array} List of all candidates
   */
  getAllCandidates() {
    return this.database.candidates;
  }

  /**
   * Get a specific candidate by ID
   * @param {string} candidateId - Candidate ID
   * @returns {object} Candidate object or null
   */
  getCandidateById(candidateId) {
    return this.database.candidates.find((c) => c.id === candidateId) || null;
  }

  /**
   * Cast a vote for a candidate
   * @param {string} voterId - Unique voter identifier
   * @param {string} candidateId - ID of candidate to vote for
   * @returns {object} Result of voting action
   */
  castVote(voterId, candidateId) {
    // Validate voter ID
    if (!voterId || voterId.trim() === "") {
      return { success: false, error: "Voter ID is required" };
    }

    // Check if voter has already voted (one vote per voter)
    if (this.database.voters.has(voterId)) {
      return { success: false, error: "You have already voted" };
    }

    // Validate candidate ID
    const candidate = this.getCandidateById(candidateId);
    if (!candidate) {
      return { success: false, error: "Invalid candidate ID" };
    }

    // Record the vote
    const vote = new Vote(voterId, candidateId);
    this.database.votes.push(vote);
    candidate.addVote();
    this.database.voters.add(voterId);

    return {
      success: true,
      message: `Vote cast successfully for ${candidate.name}`,
      vote: vote,
    };
  }

  /**
   * Get voting results
   * @returns {object} Results with candidates and their vote counts
   */
  getResults() {
    const totalVotes = this.database.votes.length;
    const results = this.database.candidates.map((candidate) => ({
      id: candidate.id,
      name: candidate.name,
      voteCount: candidate.voteCount,
      percentage: candidate.getVotePercentage(totalVotes),
    }));

    // Sort by vote count in descending order
    results.sort((a, b) => b.voteCount - a.voteCount);

    return {
      totalVotes: totalVotes,
      totalCandidates: this.database.candidates.length,
      results: results,
    };
  }

  /**
   * Get winner(s) of the election
   * @returns {object} Winner information
   */
  getWinner() {
    if (this.database.candidates.length === 0) {
      return { success: false, error: "No candidates in the election" };
    }

    const results = this.getResults();
    if (results.totalVotes === 0) {
      return { success: false, error: "No votes cast yet" };
    }

    const maxVotes = Math.max(...this.database.candidates.map((c) => c.voteCount));
    const winners = this.database.candidates.filter((c) => c.voteCount === maxVotes);

    return {
      success: true,
      winners: winners.map((w) => ({ id: w.id, name: w.name, votes: w.voteCount })),
      totalVotes: results.totalVotes,
    };
  }

  /**
   * Get total number of votes cast
   * @returns {number} Total votes
   */
  getTotalVotes() {
    return this.database.votes.length;
  }

  /**
   * Reset the voting system
   * @returns {object} Confirmation message
   */
  resetVoting() {
    this.database.candidates = [];
    this.database.votes = [];
    this.database.voters.clear();

    return { success: true, message: "Voting system has been reset" };
  }

  /**
   * Check if a voter has already voted
   * @param {string} voterId - Voter ID
   * @returns {boolean} True if voter has voted, false otherwise
   */
  hasVoterVoted(voterId) {
    return this.database.voters.has(voterId);
  }
}

// Export for use in other modules
if (typeof module !== "undefined" && module.exports) {
  module.exports = { VotingSystem, Candidate, Vote };
}