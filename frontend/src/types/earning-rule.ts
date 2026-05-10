export interface EarningRuleResponse {
  id: number
  tierId: number
  tierName: string
  earningType: string
  multiplier: number
  fixedPoints: number
  description: string
  conditions: string
  active: boolean
  priority: number
}

export interface CreateEarningRuleRequest {
  tierId: number
  earningType: string
  multiplier: number
  fixedPoints: number
  description: string
  conditions: string
  active: boolean
  priority: number
}

export type UpdateEarningRuleRequest = CreateEarningRuleRequest