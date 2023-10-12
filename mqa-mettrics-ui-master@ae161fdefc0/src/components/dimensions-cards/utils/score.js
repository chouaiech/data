export function scoreLevel(score, identifier) {
  score = score * (maxScore() / maxScore(identifier)); // normalize to 405 max
  if (score > 350) return 3;
  if (score > 220) return 2;
  if (score > 120) return 1;
  if (score >= 0) return 0;
  return -1;
}

export function maxScore(identifier) {
  switch(identifier) {
    case 'accessibility': return 100;
    case 'findability': return 100;
    case 'interoperability': return 110;
    case 'reusability': return 75;
    case 'contextuality': return 20;
    default: return 405;
  }
}

export function scoreClasses(score, identifier) {
  const level = scoreLevel(score, identifier);
  return {
    'score--unknown': level === -1,
    'score--bad': level === 0,
    'score--sufficient': level === 1,
    'score--good': level === 2,
    'score--excellent': level === 3,
  };
}
